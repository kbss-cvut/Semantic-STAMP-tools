

import React, {Fragment, PureComponent} from 'react';
// import Reflux from 'reflux';
// import ReactDOM from 'react-dom';
import {includes} from 'lodash';


import {Treebeard, decorators} from 'react-treebeard';

// import {Div} from "./common";
import styles from './styles';
import * as filters from './filter';
import Header from './Header';
import NodeViewer from './NodeViewer';
import OptionsStore from '../../stores/OptionsStore';
import Constants from "../../constants/Constants";
import Vocabulary from "../../constants/Vocabulary";
// import I18nMixin from '../../i18n/I18nMixin';
import Actions from '../../actions/Actions';

class TreeSelect extends PureComponent {

    constructor(props) {
        super(props);
        // this.mixins = [Reflux.listenTo(OptionsStore, '_onOptionsLoaded')];
        this.state = {
            data : {},
            initialized : false,
            value: props.value
        };
        this.styles=styles;
        this.data = {};
        this.finterInProgress = false;
        this.filterValue = '';
        this.onToggle = this.onToggle.bind(this);
        this.onSelect = this.onSelect.bind(this);
        this._onOptionsLoaded = this._onOptionsLoaded.bind(this);
    }

    componentDidMount() {
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE);
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE_PART_WHOLE_RELATION);
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    // componentWillUnmount() {
    //
    // }


    _onOptionsLoaded(){
        var nodes = OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE);
        var edges = OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE_PART_WHOLE_RELATION);
        if(!this.state.initialized && nodes && nodes.length > 0 && edges && edges.length > 0){
            this.setState({initialized : true, data : this._constructData(nodes, edges)})
            this.unsubscribe();
        }
    }

    _constructData(nodes, edges){
        // data example
        // {
        //     name: 'react-treebeard',
        //         toggled: true,
        //     children: [
        //     {
        //         name: 'example',
        //         children: [
        //             { name: 'app.js' },
        //             { name: 'data.js' },
        //             { name: 'index.html' },
        //             { name: 'styles.js' },
        //             { name: 'webpack.config.js' }
        //         ]
        //     },
        // transform nodes


        var nodeMap = new Map();
        nodes.forEach((n) => {
            var node = {
                id: n['@id'],
                name : n[Vocabulary.RDFS_LABEL],
                toggled: false,
            };
            nodeMap.set(node.id, node);
        });

        let childNodes = [];
        edges.forEach((e) => {
            let n = nodeMap.get(e['@id']);
            if(n && e[Vocabulary.HAS_STRUCTURE_PART] && e[Vocabulary.HAS_STRUCTURE_PART].length > 0) {
                n.children = e[Vocabulary.HAS_STRUCTURE_PART].map((n) => nodeMap.get(n['@id']));
                childNodes.push(...n.children);
            }
        });

        // find roots
        let roots = null;
        let fromNode = null;
        if(this.props.from){
            fromNode = nodeMap.get(this.props.from);
            if(fromNode)
                roots = fromNode.children;
        }

        if(!roots){
            childNodes.forEach(n => nodeMap.delete(n.id));
            roots = nodeMap.values();
        }

        let root = {};
        if (roots.length == 1) {
            root = roots[0];
        } else {
            root = {
                name: '',
                children: [...roots],
                toggled: false
            }
        }
        this.data = root;
        return root;
    }


    onToggle(node, toggled) {
        const {cursor, data} = this.state;
        // var tmp = {cursor, active: false};

        if (cursor) {
            cursor.active = false;
            // this.setState(() => ({cursor: {active: false}}), this.myStateChange);
        }
        const toggleTime = new Date().getTime();
        if( !this.props.disabled &&
            this.lastToggleTime && toggleTime - this.lastToggleTime < 400 && cursor && cursor.id === node.id){
            // this.onSelect(node);
            this.onSelect(node);

            this.lastToggleTime = null;
        }else{
            this.lastToggleTime = toggleTime;
        }

        node.active = true;
        if (node.children) {
            node.toggled = toggled;
        }

        this.setState(() => ({cursor: node, data: Object.assign({}, data)}));
    }

    // myStateChange(){
    //     var st = this.state;
    //     var i = 10;
    // }

    onSelect(node) {
        this.setState({selected: node, value: node.name});
        if(this.props.onSelect) {
            this.props.onSelect(node);
            // this.searchBox.value = node.name;
        }
        // const {cursor, data} = this.state;
        // if (cursor) {
        //     this.setState(() => ({cursor, active: false}));
        //     if (!includes(cursor.children, node)) {
        //         cursor.toggled = false;
        //         cursor.selected = false;
        //     }
        // }
        //
        // node.selected = true;
        //
        // this.setState(() => ({cursor: node, data: Object.assign({}, data)}));
    }

    onFilterMouseUp({target: {value}}) {
        this.filterValue = value.trim();
        const filter = this.filterValue;
        const data = this.data;
        let filtered = filters.filterTree(data, filter);
        filtered = filters.expandFilteredNodes(filtered, filter);
        this.setState(() => ({data: filtered}));

        // setTimeout(() =>{
        //     if(this.finterInProgress)
        //         return;
        //     this.finterInProgress = true;
        //     if (!filter) {
        //         return this.setState(() => ({data}));
        //     }
        //     let filtered = filters.filterTree(data, filter);
        //     filtered = filters.expandFilteredNodes(filtered, filter);
        //     this.setState(() => ({data: filtered}));
        //     this.finterInProgress = false;
        // }, 700);
    }

    render() {
        const {data, cursor} = this.state;
        const nodeStyle = styles.node;

        const searchBoxVal = {};
        if(this.oldValue != this.state.value){
            this.oldValue = this.state.value;
            searchBoxVal.value = this.state.value;
        }

        return (<div>
                <div style={this.styles.searchBox}>
                    <label>{this.props.label}</label>
                    <div className="input-group">
                        <span className="input-group-addon">
                            <i className="fa fa-search"/>
                        </span>
                        <input
                            ref={c => this.searchBox = c}
                            className="form-control"
                            onKeyUp={this.onFilterMouseUp.bind(this)}
                            placeholder="Search for processes, activities and deviations ..."
                            type="text"
                            disabled={this.props.disabled}
                            {...searchBoxVal}
                        />
                        {/*{this.state.selected ?*/}
                        {/*    <input*/}
                        {/*        className="form-control"*/}
                        {/*        onKeyUp={this.onFilterMouseUp.bind(this)}*/}
                        {/*        placeholder="Search for processes, activities and deviations ..."*/}
                        {/*        type="text"*/}
                        {/*        value={this.state.selected.name}/> :*/}
                        {/*    <input className="form-control"*/}
                        {/*        onKeyUp={this.onFilterMouseUp.bind(this)}*/}
                        {/*        placeholder="Search for processes, activities and deviations ..."*/}
                        {/*        type="text"/>*/}
                        {/*}*/}
                    </div>
                </div>
                <div style={styles.component}>
                    <Treebeard
                        ref={c => this.treebeard = c}
                        data={data}
                        onToggle={this.onToggle}
                        // onSelect={this.onSelect}
                        decorators={{...decorators, Header}}
                        style={this.styles.treeStyle}
                         // customStyles={}
                    />
                </div>
                {/*<div style={styles.component}>*/}
                {/*    <NodeViewer node={cursor}/>*/}
                {/*</div>*/}
            </div>
        );
    }
}

export default TreeSelect;