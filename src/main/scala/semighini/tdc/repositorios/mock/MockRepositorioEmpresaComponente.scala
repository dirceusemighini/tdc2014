package semighini.tdc.repositorios.mock

import semighini.tdc.modelo.Empresa
import semighini.tdc.repositorios.RepositorioEmpresaComponente

/**
 * Created by dirceu on 8/7/14.
 */
trait MockRepositorioEmpresaComponente extends RepositorioEmpresaComponente{

  override val empresaRepositorio = new RepositorioEmpresa {

    private val mockEmpresa = Some(Empresa("Empresa do Zequinha Ltda","12.123.123.0001/01","Bar do Zé"))
    override def buscaPorCNPJ(cnpj: String): Option[Empresa] = mockEmpresa

  }
}
