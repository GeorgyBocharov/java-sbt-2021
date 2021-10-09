package ru.sbt.reflection.parser.factory;

import net.openhft.compiler.CompilerUtils;
import ru.sbt.reflection.parser.generator.JsonGenerator;
import ru.sbt.reflection.parser.utils.KeyValue;


public final class GeneratorFactory {

    private GeneratorFactory() {}

    public static <T> JsonGenerator<T> createJsonGenerator(Class<T> item){

        KeyValue<String, String> keyValue = GeneratorStringBuilder.createGeneratorString(item);
        System.out.println("Got KeyValue " + keyValue);
        try {
            Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(keyValue.getKey(), keyValue.getValue());
            return (JsonGenerator<T>) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
