package com.example.demoflux.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;


@RestController
@CrossOrigin
public class DemoController {

    @CrossOrigin
    @PostMapping("/getInfo")
    @ResponseBody
    public String info() {
        return "It's DEMO!!!!";
    }

    @CrossOrigin
    @GetMapping("/just")
    Flux<String> just() {

        Flux<String> fx = Flux.just("alpha-11qq", "bravo--", "charlie--");
        fx.subscribe(System.out::println);
        return fx;

    }

    @GetMapping("/just1")
    Flux<String> just1() {

        Flux.just("alpha", "bravo", "charlie")
                .map(String::toUpperCase)
                .flatMap(s -> {
                    System.out.println("#### sss ::" + s);
                    return Flux.fromArray(s.split(""));
                })
                .groupBy(String::toString)
                .sort((o1, o2) -> {
                    System.out.println("#### o1o2 ::" + o1.key() + " --- " + o1);
                    return o1.key().compareTo(o2.key());
                })
                .flatMap(group -> Mono.just(group.key()).and(group.count()))
                .map(keyAndCount -> {
                            System.out.println("####" + keyAndCount.getClass().getName());
                            //keyAndCount.getT1() + " => " + keyAndCount.getT2();
                            return keyAndCount;
                         }
                    )
                .subscribe(System.out::println);

        return null;

    }

    @GetMapping("/str")
    Flux<String> str() {
        Queue<String> lst = new ConcurrentLinkedDeque<String>();
        for(int i = 0; i < 10; i++) {
            lst.add("-----processing -- num -:" + i);
        }

        Flux<String> fx = Flux.interval(Duration.ofSeconds(1)).fromStream(lst.stream()).map(str -> str.toUpperCase());
        //fx.subscribe(System.out::println);

//        for(int i = 100; i < 110; i++) {
//            lst.add("-----processing -- num -:" + i);
//        }
        return fx;

    }

}
