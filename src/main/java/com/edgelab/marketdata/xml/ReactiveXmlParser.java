package com.edgelab.marketdata.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static java.util.Optional.empty;

@Component
@RequiredArgsConstructor
public class ReactiveXmlParser {

    private final XmlMapper xmlMapper;
    private final XMLInputFactory xmlInputFactory;

    public <T> Flux<T> parse(InputStream inputStream, Class<T> type, String tagName) {
        return Flux.generate(
            () -> createXmlReader(inputStream),
            (r, sink) -> process(r, sink, type, tagName),
            this::terminate
        );
    }

    private XMLStreamReader createXmlReader(InputStream inputStream) {
        try {
            return xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> XMLStreamReader process(XMLStreamReader reader, SynchronousSink<T> sink, Class<T> type, String tagName) {
        Optional<T> node = nextNode(reader, type, tagName);

        if (node.isPresent()) {
            sink.next(node.get());
        } else {
            sink.complete();
        }

        return reader;
    }

    private <T> Optional<T> nextNode(XMLStreamReader r, Class<T> type, String tagName) {
        try {
            while (r.hasNext()) {
                int event = r.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (r.getLocalName().equals(tagName)) {
                            return Optional.of(parseNode(r, type));
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (!r.hasNext()) {
                            return empty();
                        }
                        break;
                }
            }

            return empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T parseNode(XMLStreamReader r, Class<T> type) throws IOException {
        return xmlMapper.readValue(r, type);
    }

    private void terminate(XMLStreamReader reader) {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

}
