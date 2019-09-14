/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.we;

import java.util.List;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Performer {
    protected List<Role> roles;
    protected List<HasRole> hasRoles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<HasRole> getHasRoles() {
        return hasRoles;
    }

    public void setHasRoles(List<HasRole> hasRoles) {
        this.hasRoles = hasRoles;
    }
}
