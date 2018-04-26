package com.edgelab.marketdata;

import com.edgelab.marketdata.config.LeaderElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final LeaderElectionService leaderElectionService;

    @GetMapping(value = "/leader")
    public boolean leader() {
        return leaderElectionService.isLeader();
    }

}
