package cz.cvut.kbss.reporting.service.data;

import java.util.Map;

public interface DataLoader {

    /**
     * Loads data from the specified source.
     *
     * @param source Source of the data
     * @param params Loading parameters. These can be e.g. query parameter for a remote request.
     * @return The loaded data as String
     */
    String loadData(String source, Map<String, String> params);

    /**
     * Loads data from the specified <code>source</code> using the specified <code>query</code> using post.
     *
     * @param source Source of the data
     * @param query Query over the data
     * @param params Loading parameters. These can be e.g. query parameter for a remote request.
     * @return The loaded data as String
     */
    default String loadData(String source, String query, Map<String, String> params){
        return "";
    }
}
