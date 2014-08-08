package semighini.tdc.repositorios

import semighini.tdc.modelo.Empresa

/**
 * Created by dirceu on 8/7/14.
 */
trait RepositorioEmpresaComponente {

  val empresaRepositorio : RepositorioEmpresa

  trait RepositorioEmpresa {

    def buscaPorCNPJ(cnpj:String) :Option[Empresa]

  }
}
