'use strict';

const Reflux = require('reflux');
const jsonld = require('jsonld');

const Actions = require('../actions/Actions');
const Constants = require('../constants/Constants');
const Vocabulary = require('../constants/Vocabulary');
const Ajax = require('../utils/Ajax');
const Logger = require('../utils/Logger');

const options = {};
const processFlows = {};

const BASE_URL = Constants.REST_PREFIX + 'schema';
const BASE_URL_WITH_SLASH = BASE_URL + '/';
const IMPORT_URL = BASE_URL_WITH_SLASH + 'import';

const OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
        this.listenTo(Actions.importSchema, this.onImportSchema);
        this.listenTo(Actions.loadProcessFlow, this.onLoadProcessFlow);
        this.trueOptionTypeMap = new Map();
        this.trueOptionTypeMap.set(Constants.OPTIONS.EVENT_TYPE, Vocabulary.EVENT_TYPE )
        this.trueOptionTypeMap.set(Constants.OPTIONS.FACTOR_TYPE, Vocabulary.FACTOR_EVENT_TYPE )
        this.trueOptionTypeMap.set(Constants.OPTIONS.LOSS_EVENT_TYPE, Vocabulary.LOSS_EVENT_TYPE )
        this.trueTypeMap = new Map();
    },

    onImportSchema: function (schemaFile, onSuccess, onError){
        var i = 1;
        console.log("importing schema");
        Ajax.post(IMPORT_URL).attach(schemaFile.file).end(function (data, resp) {
            if (onSuccess) {
                onSuccess();
            }
            // update option store
        }.bind(this), onError);
    },

    onLoadProcessFlow: function(processURL){
        Ajax.get(BASE_URL_WITH_SLASH + 'eventTypeFlow?process=' + processURL).end(function (data) {
            if (data.length > 0) {
                let processFlow = {
                    nodes: [],
                    edges: []
                };
                data.forEach((n) => {
                    processFlow.nodes.push(n['@id']);
                    if(n[Vocabulary.EVENT_FLOW_NEXT]) {
						let following = n[Vocabulary.EVENT_FLOW_NEXT];
						if(!following.length)
							following = [following];
                        following.forEach((cn) => {
							processFlow.nodes.push(cn['@id']);
                            processFlow.edges.push({
                                from: n['@id'],
                                to: cn['@id']
                            });
                        });
                    }
                });
				processFlow.nodes = [... new Set(processFlow.nodes)];
				processFlows[processURL] = processFlow;
                this.trigger(processURL, processFlows[processURL]);
            } else {
                Logger.warn('No data received when loading process flow <' + processURL + '>.');
                this.trigger(processURL, this.getProcessFlow(processURL));
            }

        }.bind(this), function () {
            this.trigger(processURL, this.getProcessFlow(processURL));
        }.bind(this));
    },

    onLoadOptions: function (type) {
        if (type) {
            this._loadOptions(type);
        } else {
            this._loadOptions(Constants.OPTIONS.OCCURRENCE_CLASS);
        }
    },

    _loadOptions: function (type) {
        if (options[type] && options[type].length !== 0) {
            this.trigger(type, options[type]);
            return;
        }
        Ajax.get(Constants.REST_PREFIX + 'options?type=' + type).end(function (data) {
            if (data.length > 0) {
                jsonld.frame(data, {}, null, function (err, framed) {
                    options[type] = framed['@graph'];
                    this.trigger(type, options[type]);
                }.bind(this));
            } else {
                Logger.warn('No data received when loading options of type ' + type + '.');
                this.trigger(type, this.getOptions(type));
            }

        }.bind(this), function () {
            this.trigger(type, this.getOptions(type));
        }.bind(this));
    },

    getOptions: function (type) {
        return options[type] ? options[type] : [];
    },

    getProcessFlow: function (processUrl) {
        return processFlows[processUrl];
    }
});

module.exports = OptionsStore;
