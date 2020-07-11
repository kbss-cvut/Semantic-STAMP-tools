package cz.cvut.kbss.datatools.bpm2stampo.common.refs.model;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class Join<F,T>{

    protected Class<F> from;
    protected Class<T> to;
//        protected List<Pair<Field,Field>> fromFields;
//        protected List<Field> toFields;
    protected List<Pair<Field,Field>> fromToMap;

    public void setFrom(Class<F> from) {
        this.from = from;
    }

    public void setTo(Class<T> to) {
        this.to = to;
    }

    public List<Pair<Field, Field>> getFromToMap() {
        return fromToMap;
    }

    public void setFromToMap(List<Pair<Field, Field>> fromToMap) {
        this.fromToMap = fromToMap;
    }

    public void sortKey(){
        fromToMap.sort(Comparator.comparing(p -> p.getLeft().getName()));
    }

    //        public List<Field> getFromFields() {
//            return fromFields;
//        }
//
//        public void setFromFields(List<Field> fromFields) {
//            this.fromFields = fromFields;
//        }
//
//        public List<Field> getToFields() {
//            return toFields;
//        }
//
//        public void setToFields(List<Field> toFields) {
//            this.toFields = toFields;
//        }

    protected String toString(Collection<Pair<Field, Field>> fs){
        return String.format("key(%s)", fs.stream()
                .map(p -> p.getLeft().getName() + " => " + p.getRight().getName())
                .collect(Collectors.joining(", ")));
    }

    public String calcFromId(F inst){
        return calcIDImpl(inst, p -> p.getLeft());
    }

    public String calcToId(T inst){
        return calcIDImpl(inst, p -> p.getRight());
    }

    protected String calcIDImpl(Object inst, Function<Pair<Field,Field>, Field> func){
        return fromToMap.stream().map(p -> getValue(inst, func.apply(p) )).collect(Collectors.joining());
    }

    protected String getValue(Object inst, Field f){
        try {
            f.setAccessible(true);
            return Objects.toString(f.get(inst));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Join{" +
                "from=" + from.getCanonicalName() +
                ", to=" + to.getCanonicalName() +
                ", fromToMap=" + toString(fromToMap) +
                '}';
    }
}
