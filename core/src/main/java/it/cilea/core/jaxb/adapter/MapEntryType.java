package it.cilea.core.jaxb.adapter;
 
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;
 
/**
 *
 * @author John Yeary
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MapEntryType<K, V> {
 
    private K key;
    private V value;
 
    public MapEntryType() {
    }
 
    public MapEntryType(Map.Entry<K, V> e) {
        key = e.getKey();
        value = e.getValue();
    }
 
    @XmlElement
    public K getKey() {
        return key;
    }
 
    public void setKey(K key) {
        this.key = key;
    }
 
    
    @XmlCDATA
    public V getValue() {
        return value;
    }
 
    public void setValue(V value) {
        this.value = value;
    }
}