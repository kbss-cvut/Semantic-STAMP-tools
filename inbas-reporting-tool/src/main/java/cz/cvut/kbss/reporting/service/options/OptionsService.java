package cz.cvut.kbss.reporting.service.options;

import cz.cvut.kbss.reporting.rest.dto.model.RawJson;

public interface OptionsService {

    /**
     * Gets options of the specified type.
     *
     * @param type Options type. It can be:
     *             <ul>
     *             <li>The name of a file containing a query to retrieve the corresponding options,</li>
     *             <li>The name of a file containing the options themselves,</li>
     *             <li>A value corresponding to one of the {@link OptionType} items.</li>
     *             </ul>
     * @return Object representing the options. This will most often be a {@link RawJson} containing options retrieved
     * from a remote repository. Otherwise, it can be an array/list of options.
     * @throws IllegalArgumentException When the specified option type is not supported
     */
    Object getOptions(String type);

    /**
     * Predefined option types which are not determined by searching for query and options files at application
     * startup.
     *
     * @see OptionsService#getOptions(String)
     */
    enum OptionType {

        /**
         * Occurrence categories used by reports in the database.
         */
        EXISTING_OCCURRENCE_CATEGORIES("existingOccurrenceCategories"),
        /**
         * Event types used by events in the database.
         */
        EXISTING_EVENT_TYPES("existingEventTypes");

        private final String name;

        OptionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
