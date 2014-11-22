package cz.cvut.kbss.inbas.audit.model;

public class Report {

	// textual description of the report
	private String description;

	// list of resources attached to the report
	private List<ElectronicResource> hasEvidence;

	// timestamp of the report creation
	private Date dateCreated;

	// Set of user-defined tags
	private Set<String> tags;

	// Set of category identifiers
	private Set<String> terms;

}
