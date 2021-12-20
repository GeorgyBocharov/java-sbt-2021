package com;

import com.xml.parser.EmptyTagsRemover;
import com.xml.parser.impl.EmptyTagsViaRecursionRemover;
import com.xml.parser.impl.EmptyTagsViaRegexRemover;
import com.xml.parser.impl.EmptyTagsViaStringIterationRemover;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class BenchmarkEmptyTagsRemoval {

    private final EmptyTagsRemover emptyTagsViaRecursionRemover = new EmptyTagsViaRecursionRemover();
    private final EmptyTagsRemover emptyTagsViaRegexRemover = new EmptyTagsViaRegexRemover();
    private final EmptyTagsRemover emptyTagsViaStringIterationRemover = new EmptyTagsViaStringIterationRemover();
    private String xmlString;


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkEmptyTagsRemoval.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        xmlString = loadFile("example.xml");
    }

    private static String loadFile(String path) throws IOException {
        Path resourceDirectory = Paths.get("src","main", "resources", path);
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return Files.readString(Path.of(absolutePath));
    }


    @Benchmark
    @Warmup(iterations = 3)
    public void testEmptyTagsViaRecursionRemover(Blackhole blackhole) {
        blackhole.consume(
                emptyTagsViaRecursionRemover.removeEmptyTags(xmlString)
        );
    }

    @Benchmark
    @Warmup(iterations = 3)
    public void testEmptyTagsViaRegexRemover(Blackhole blackhole) {
        blackhole.consume(
                emptyTagsViaRegexRemover.removeEmptyTags(xmlString)
        );
    }

    @Benchmark
    @Warmup(iterations = 3)
    public void testEmptyTagsViaStringIterationRemover(Blackhole blackhole) {
        blackhole.consume(
                emptyTagsViaStringIterationRemover.removeEmptyTags(xmlString)
        );

    }
}
