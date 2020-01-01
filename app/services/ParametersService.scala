package services

import javax.inject.Inject
import services.dao.ParametersDAO

class ParametersService @Inject() (val parametersDAO: ParametersDAO) {

  def getAllParameters = parametersDAO.getAllParameters

}
