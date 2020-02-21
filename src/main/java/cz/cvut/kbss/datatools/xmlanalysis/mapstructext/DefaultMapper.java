package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel.BaseXMLEntity;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.Identifiable;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DefaultMapper<T> {
    String[] emptyStringArray = new String[0];


    default String iri(T o){
        return iri(o, emptyStringArray, emptyStringArray);
    }

    default String iri(T o, String[] beforeId, String[] afterId){
        return getProcessorContext().toIRI(o, beforeId, afterId);
    }

    default Set<String> iris(T o){
        return iris(o, null, null);
    }

    default Set<String> iris(T o, String[] beforeId, String[] afterId){
        return iris(Arrays.asList(o));
    }

    default Set<String> iris(Collection<? extends T> inC){
//        return iris(inC, null, null);
        return iris(inC, emptyStringArray, emptyStringArray);
    }

    default Set<String> iris(Collection<? extends T> inC, String[] beforeId, String[] afterId){
        return new HashSet<>(Optional.ofNullable(inC)
                .map(c -> c.stream()
                        .filter(o -> o != null)
                        .map(o -> iri(o, beforeId, afterId)).collect(Collectors.toSet()))
                .orElse(Collections.EMPTY_SET));
    }

    default Set<String> iris(Collection<? extends T> ... collections){
        return iris(null, null, collections);
    }

    default Set<String> iris(String[] beforeId, String[] afterId, Collection<? extends T> ... collections){
        return new HashSet<>(Stream.of(collections)
                .filter(c -> c != null)
                .flatMap(c -> c.stream())
                .map(this::iri)
                .collect(Collectors.toCollection(HashSet::new)));
    }

    default Set<String> getAndInit(Supplier<Set<String>> get, Consumer<Set<String>> set){
        Set<String> s = get.get();
        if(s == null){
            s = new HashSet<>();
            set.accept(s);
        }
        return s;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Registry API /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    default BPMProcessor getProcessorContext(){
        return BPMProcessor.getProcessor(this);
    }

    /**
     * Implement in Specific mapper.
     * @return
     */
    default Map<String, Identifiable> getRegistry(){
        return getProcessorContext().getRegistry();
    }

    /**
     * Call this method to execute a transformer method inside of another transformer method. This method makes sure
     * that the called transformer method's decorator annotations are executed.
      * @param methodName
     * @param e
     * @param outClass
     * @param <T>
     * @return
     */
    default <T extends Identifiable> T transform(String methodName, BaseXMLEntity e, Class<T> outClass){
        return getProcessorContext().getMapstructProcessor().transform(methodName, e, outClass);
    }

    @AfterMapping
    default void applyMapperExtension(T in, @MappingTarget Identifiable out){
        BPMProcessor processor = getProcessorContext();
        Transformer transformer = processor.getMapstructProcessor().getTransformStack().peek().getTransformer();
        transformer.applyExtendedMappings(in, out);

        if(out != null)
            getRegistry().put(out.getIri(), out);
    }

    @Deprecated
    default <F> F get(T in, Class<F> cls){
        Object identifiable = getRegistry().get(iri(in));
        if(identifiable != null && cls.isAssignableFrom(identifiable.getClass())){
            return (F)identifiable;
        }
        return null;
    }

    default Identifiable get(T in){
        return getRegistry().get(iri(in));
    }

    default Identifiable get(String in){
        return getRegistry().get(in);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Default Model Utils //////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// String utils /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    default String def(String val, String defaultValue){
        Optional.ofNullable(val).orElse(defaultValue);
        return val != null && !val.isEmpty() ? val : defaultValue;
    }


    default String concat(String sep, String ... comps){
        return String.join(sep, comps);
    }

    default String composeLabel(String first, String nullableSecond, String def){
        return first != null && !first.isEmpty() ?  first + " - " + nullableSecond : def;
    }

    default String composeLabel(String first, String nullableSecond){
        return composeLabel(first, nullableSecond, null);
    }
}
