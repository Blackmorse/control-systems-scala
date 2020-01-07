package services

import javax.inject.Inject
import services.dao.{DocumentsDAO, ParametersDAO}

class ParametersService @Inject() (val parametersDAO: ParametersDAO,
                                   val documentsDAO: DocumentsDAO) {

  def getAllParameters = parametersDAO.getAllParameters
}
