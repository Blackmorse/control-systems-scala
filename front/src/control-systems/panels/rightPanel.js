import React from 'react'
import './rightPanel.css'

class RightPanel extends React.Component {

    // constructor(props) {
    //     super(props);
    //     // this.state={
    //     //     selected: props.selected
    //     // };
    //     this.selected=props.value.selected
    //     this.links=props.value.links
    // }

    render() {
        return <aside className="right_panel">
            <nav className="right_panel_navigation">
            
                {this.props.value.links.map(link => {
                    return <a className={"nav_link" + (this.props.value.selected === link.title ? " selected_page" : "")} 
                            href="#" onClick={link.onClick} key={link.title}>
                            <span className="nav_link_text">
                                {link.title}
                            </span>
                        </a>
                })}
        </nav>
      </aside>
    }
}

export default RightPanel