import React, {useEffect, useState} from 'react';
import {getAllParameters, updateParameter, addParameter} from './client/RestClient';
import RightPanel from './panels/rightPanel';
import TopPanel from './panels/TopPanel';
import './parameters.css';
import ParameterEntity from './models/ParameterEntity'
import {FormControl} from 'react-bootstrap';
import {TypedUseSelectorHook, useSelector} from 'react-redux';
import {LoginState} from './login/Login';

const ParametersPage = () => {
    const useAppSelector: TypedUseSelectorHook<LoginState> = useSelector
    let stateMapFunc = (state: LoginState) => state?.login
    let token = useAppSelector(stateMapFunc)?.token

    const [allParameters, setParameters] = useState(([] as ParameterEntity[]))
    const [isLoading, setIsLoading] = useState(true)
    const [editingId, setEditingId] = useState(undefined as number | undefined)
    const [[name, unit, defaultValue], setEditingParameterValues] = useState(['', '', ''] as [string, string, string])

    const [isAddingNewParameter, setAddingNewParameter] = useState(false)
    const [[newId, newName, newUnit, newDefaultValue], setNewParameterValues] = useState(['', '', '', ''])

    //Для того, чтобы обновлять все параметры , когда надо
    const [version, setVersion] = useState(1)
    useEffect(() => {
        setIsLoading(true)
        getAllParameters(res => {
            setIsLoading(false)
            setParameters(res)
        })
    }, [version])

    let rightPanelProps = {
      links: [{
        title: "Все параметры",
        onClick: function(){}
      }],
      selected: "Все параметры"
    }

    let saveParameter = (id: number) => {
        updateParameter(token!, id, name, unit, defaultValue, 
        () => {
            setEditingId(undefined)
            setVersion(version + 1)
        }, 
        () => {
            alert('Не удалось обновить параметр')
            setEditingId(undefined)
            setVersion(version + 1)
        })
    }

    let addParameterFunc = () => {
        addParameter(token!, newId, newName, newUnit, newDefaultValue,
        () => {
            setAddingNewParameter(false)
            setVersion(version + 1)
        },
        () => {
            alert('Не удалось добавить параметр')
            setAddingNewParameter(false)
            setVersion(version + 1)
        })
    }


    let content: JSX.Element
    if (isLoading) {
        content = <span>Загрузка</span>
    } else {

        let addParameterRow = (isAddingNewParameter) ?
                <>
                    <td><FormControl type='text' style={{width: '100%'}} onChange={(e) => setNewParameterValues([e.target.value, newName, newUnit, newDefaultValue])} /></td>
                    <td><FormControl type='text' style={{width: '100%'}} onChange={(e) => setNewParameterValues([newId, e.target.value, newUnit, newDefaultValue])} /></td>
                    <td><FormControl type='text' style={{width: '100%'}} onChange={(e) => setNewParameterValues([newId, newName, e.target.value, newDefaultValue])} /></td>
                    <td><FormControl type='text' style={{width: '100%'}} onChange={(e) => setNewParameterValues([newId, newName, newUnit, e.target.value])} /></td>
                    <td><button onClick={() => addParameterFunc()}>Добавить</button></td>
                </>
            :
                <td colSpan={3}><button onClick={() => setAddingNewParameter(true)}>Добавить</button></td>
            

      content = <table className="red_table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Имя характеристики</th>
            <th>Единицы</th>
            <th>Значение по умолчанию</th>
          </tr>
        </thead>
        <tbody>
          {allParameters.map(parameter => {
            return <tr key={'parameter_row_' + parameter.id} onClick={() => {if (parameter.id !== editingId) { setEditingId(undefined)}; setAddingNewParameter(false)}} onDoubleClick={() => {
                    if (token !== undefined) {
                        setEditingId(parameter.id)
                        setEditingParameterValues([parameter.name, parameter.unit, parameter.defaultValue])
                    }
                }}>
              <td>{parameter.id}</td>
              {(editingId !== parameter.id) ? <>
                  <td>{parameter.name}</td>
                  <td>{parameter.unit}</td>
                  <td>{parameter.defaultValue}</td>
              </> 
              :
              <>
                  <td><FormControl type='text' style={{width: '100%'}} onChange={(e) => setEditingParameterValues([e.target.value, unit, defaultValue])} defaultValue={parameter.name} /></td>
                  <td><FormControl type='text' onChange={(e) => setEditingParameterValues([name, e.target.value, defaultValue])} defaultValue={parameter.unit} /></td>
                  <td><FormControl type='text' onChange={(e) => setEditingParameterValues([name, unit, e.target.value])} defaultValue={parameter.defaultValue}/></td>
                  <td><button onClick={() => saveParameter(parameter.id)}>Сохранить</button></td>
              </>}
            </tr>
          })}
          
            {(token === undefined) ? <></> : <tr>{addParameterRow}</tr>}
        </tbody>
      </table>
    }

    return <div className="full_layout" style={{ background: 'rgb(130,50,32)' }}>
    <div className="main_layout">
      <TopPanel pageName="Параметры"/>
      <div className="bottom_section">
        <section className="content">
          {content}
        </section>
        <RightPanel value={rightPanelProps}/>
      </div>
    </div>
    </div>
  }
  
export default ParametersPage
