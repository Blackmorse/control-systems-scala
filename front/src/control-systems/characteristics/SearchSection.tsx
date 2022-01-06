import React, {useEffect, useState} from 'react'
import { getAllParameters } from '../client/RestClient'
import ParameterEntity from '../models/ParameterEntity'

interface Props {
    onSearchClicked: (parameterIds: Array<number | undefined>, parameterValues: Array<string>) => void
}

const SearchSection = (props: Props) => {
    const [parameters, setParameters] = useState(new Array<ParameterEntity>())
    const [isLoading, setIsLoading] = useState(true)
    
    const [selectedParameterIds, setSelectedParameterIds] = useState(([undefined, undefined] as [number?, number?]))

    useEffect(() => {
        setIsLoading(true)
        getAllParameters((result) => {
            setParameters(result)
            setIsLoading(false)
        })
    }, [])

    const [[firstSearchText, secondSearchText], setSearchText] = useState(['', ''])

    if(isLoading) {
        return <span>Загрузка...</span>
    } 

    let onSelectedFirstParameterChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedParameterIds([Number(e.target.value), selectedParameterIds[1]] ) 
    }

    let onSelectedSecondParameterChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedParameterIds([selectedParameterIds[1], Number(e.target.value)]) 
    }

    return <section className='search_section'>
        <div className='search_section_row'>
            <select onChange={onSelectedFirstParameterChange}>
                {parameters.map(parameter => {
                    return <option value={parameter.id} key={'search_param_1_option' + parameter.id}>{parameter.name}</option>
                })}   
            </select>
            <input type="text" id={'search_text_' + 1} onChange={e => setSearchText([e.target.value, secondSearchText])}></input>
        </div>
        <div className='search_section_row'>
            <select onChange={onSelectedSecondParameterChange}>
                {parameters.map(parameter => {
                    return <option value={parameter.id} key={'search_param_2_option' + parameter.id}>{parameter.name}</option>
                })}   
            </select>
            <input type="text" id={'search_text_' + 2} onChange={e => setSearchText([firstSearchText, e.target.value])}></input>
        </div>
      <button className="green_button" onClick={(e) => {props.onSearchClicked(selectedParameterIds, [firstSearchText, secondSearchText])}}>
        Поиск
      </button>
    </section>
}

export default SearchSection
