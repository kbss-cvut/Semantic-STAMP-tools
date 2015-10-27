package cz.cvut.kbss.inbas.audit.service.options;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import org.springframework.stereotype.Service;

@Service("operator")
class OperatorsOptionsService implements OptionsService {

    private String operators;

    @Override
    public synchronized RawJson getOptions() {
        if (operators == null) {
            loadOperators();
        }
        return new RawJson(operators);
    }

    private void loadOperators() {
        this.operators = new FileOptionsLoader().load("operators.json");
    }
}
