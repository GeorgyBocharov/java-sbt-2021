package ru.sbt.reflection.parser.factory;

import net.openhft.compiler.CompilerUtils;
import ru.sbt.reflection.parser.generator.JsonGenerator;

import java.util.Collection;


public final class GeneratorFactory {

    private GeneratorFactory() {}

    public static <T> JsonGenerator<T> createJsonGenerator(T item){

        JsonParserClassCodeContainer parserCodeContainer = JsonParserCodeGenerator.createCodeContainer(item);
        try {
            Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(parserCodeContainer.getClassName(), parserCodeContainer.getClassCode());
            return (JsonGenerator<T>) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> JsonGenerator<Collection<T>> createJsonGeneratorForCollection(Collection<T> item){

        JsonParserClassCodeContainer parserCodeContainer = JsonParserCodeGenerator.createCodeContainerForCollection(item);
        try {
            Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(parserCodeContainer.getClassName(), parserCodeContainer.getClassCode());
            return (JsonGenerator<Collection<T>>) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
