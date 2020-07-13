import React from 'react'
import Plot from 'react-plotly.js';


class CharacteristicsChart extends React.Component {
    constructor(props) {
        super(props)
    }

    render() {

        var chartData=this.props.documentCharts.documentCharts.map(documentChart => {
            return {
                name: documentChart.document.fileNameParameters.objectName + ' M' + documentChart.document.fileNameParameters.objectEngineNumber,
                x: documentChart.percents,
                y: documentChart.temperatures,
                type: 'scatter'
            }
        })

        var layout = {
            showlegend: true,
            yaxis: {
               range: [200, 600],
               type: 'linear'
            },
            legend: {
              "orientation": "h"
            }
          };

        return (
          <Plot className="plotly"
            data={chartData}
            layout={layout}
          />
        );
      }
}

export default CharacteristicsChart