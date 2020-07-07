import React from 'react'
import './rightPanel.css'

class RightPanel extends React.Component {

    constructor(props) {
        super(props);
        this.state={pageName: props.pageName};
    }

    render() {
        return <aside className="right_panel">
            <nav className="right_panel_navigation">
            
                {this.props.links.map(function(link) {
                    return <a className="nav_link" href="#" onClick={link.onClick} key={link.title}>
                            {link.title}
                        </a>
                })}
        </nav>
      </aside>
    }
}

export default RightPanel