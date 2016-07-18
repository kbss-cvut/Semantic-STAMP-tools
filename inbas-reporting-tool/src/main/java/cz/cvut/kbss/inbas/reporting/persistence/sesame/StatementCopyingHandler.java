package cz.cvut.kbss.inbas.reporting.persistence.sesame;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

import java.util.Objects;

/**
 * Sesame RDF handler which takes the provided statements and adds them into the target connection.
 * <p>
 * A target context can be provided, but is optional.
 */
public class StatementCopyingHandler extends RDFHandlerBase {

    private final RepositoryConnection connection;
    private final URI context;

    public StatementCopyingHandler(RepositoryConnection connection) {
        Objects.requireNonNull(connection);
        this.connection = connection;
        this.context = null;
    }

    public StatementCopyingHandler(RepositoryConnection connection, java.net.URI context) {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(context);
        this.connection = connection;
        this.context = connection.getValueFactory().createURI(context.toString());
    }

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        try {
            connection.add(st, context);
        } catch (RepositoryException e) {
            throw new RDFHandlerException(e);
        }
    }
}
