/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.xml;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Attribute implements TypedElement, NamedElement{
    protected String name;
    protected String type;
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    
}
