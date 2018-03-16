/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.model.xml;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Instance implements ElementWithClass, NamedElement, IdntifiableElement{
    protected String id;
    protected String name;
    protected String cls;

    public Instance(String id, String name, String cls) {
        this.id = id;
        this.name = name;
        this.cls = cls;
    }

    public Instance() {
    }
    
    
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getCls() {
        return cls;
    }
    
    @Override
    public void setCls(String cls) {
        this.cls = cls;
    }
    
}
