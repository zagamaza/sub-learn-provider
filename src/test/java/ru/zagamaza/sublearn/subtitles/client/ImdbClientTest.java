package ru.zagamaza.sublearn.subtitles.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.zagamaza.sublearn.subtitles.client.imdb.ImdbClient;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@SpringBootTest
class ImdbClientTest {

    @Autowired
    ImdbClient imdbClient;

    @Test
    public void testClient() {

        System.out.println(imdbClient.search("860b2ec4", "How i met"));

    }

    @Test
    public void testClientImdbId() {

        System.out.println(imdbClient.getByImdb("860b2ec4", "tt0460649"));

    }
//
//    @Test
//    public void testStreamSimple() {
//        IntStream.range(0, 1000).boxed()
//                .parallel()
//                .peek(integer -> {
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                })
//                .forEach(System.out::println);
//        Executors.newCachedThreadPool();
//
//    }
//
//    @Test
//    public void testStreamCustomThread() {
//        final int parallelism = 20;
//        ForkJoinPool forkJoinPool = null;
//        try {
//            forkJoinPool = new ForkJoinPool(parallelism);
//            final List<Integer> primes = forkJoinPool.submit(() ->
//                    // Parallel task here, for example
//                    IntStream.range(0, 1000).boxed()
//                            .parallel()
//                            .peek(integer -> {
//                                try {
//                                    Thread.sleep(10000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            })
//                            .peek(System.out::println)
//                            .collect(toList()))
//                    .get();
//            System.out.println(primes);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (forkJoinPool != null) {
//                forkJoinPool.shutdown();
//            }
//        }
//
//    }

}