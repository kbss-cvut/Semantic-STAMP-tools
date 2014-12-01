/**
 * Copyright (c) 2014, Czech Technical University in Prague, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package cz.cvut.kbss.inbas.audit.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
