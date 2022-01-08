import React from 'react'
import {getAllParameters} from '../client/RestClient';

class DocumentsCompare extends React.Component {
    constructor(props) {
        super(props)
        this.state = {parameters: []}
    }

    componentDidMount() {
        getAllParameters((res) => {
            this.setState({parameters: res})
        })
    }

    render() {
        var documentParameterMaps = this.props.documents.map(document => {
            return document.parameters.reduce(function(map, parameter) {
                map[parameter[0].code] = parameter;
                return map;
            }, {})
        })

        return <table className="green_table">
            <thead>
              <tr>
                <th>Номер</th>
                <th>Название</th>
                {this.props.documents.map(document => {
                    return <th>{document.fileNameParameters.date + ' '
                        + document.fileNameParameters.objectName + ' '
                        + document.fileNameParameters.engineNumber} 
                        </th>
                })}
              </tr>
            </thead>
            <tbody>
                
                {this.state.parameters.map(parameter => {
                   return <tr>
                          <td>{parameter.id}</td>
                          <td>{parameter.name}</td>
                          {documentParameterMaps.map(documentParameterMap => {
                              var value = (documentParameterMap[parameter.id] ? documentParameterMap[parameter.id][1] : "")
                              return <td>
                                  {value}
                              </td>
                          })}
                        </tr>
                })}
                
            </tbody>
        </table>
    }
}

export default DocumentsCompare
