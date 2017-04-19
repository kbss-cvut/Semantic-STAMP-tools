'use strict';

import React from "react";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";

class TextAnalysisResult extends React.Component {
     static propTypes = {
         items: React.PropTypes.array.isRequired
     };

     constructor(Props) {
          super();
     }

    render () {
        const items = this.props.items;
        let html = "";
        for (let b = 0; b < this.props.items.length; b++) {
            html += "<a href='"
                + items[b].resource
                + "'>"
                + "<span class='labelTA label-default'>"

                + items[b].label

                + "</span>"
                + "</a>";
        }

        return <div style={{width:'100%', height:'150px' , overflow: 'auto'}}>

        <div style={{width:'100%', display:'table-cell'}} dangerouslySetInnerHTML={{__html: html}}>

        </div>

        </div>

    }

}

export default injectIntl(I18nWrapper(TextAnalysisResult));