package br.com.jonathan.pontointeligente.api.repositories;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jonathan.pontointeligente.api.entities.Empresa;
import br.com.jonathan.pontointeligente.api.entities.Funcionario;
import br.com.jonathan.pontointeligente.api.entities.Lancamento;
import br.com.jonathan.pontointeligente.api.enums.PerfilEnum;
import br.com.jonathan.pontointeligente.api.enums.TipoEnum;
import br.com.jonathan.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresRepository empresaRepository;
	
	private Long funcionarioId;
	@Before
	public void setUp() throws Exception{
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		this.funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
	}
	
	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorFuncioanarioId() {
		List<Lancamento> lancamento = this.lancamentoRepository.findByFuncionarioId(funcionarioId);
		
		assertEquals(2, lancamento.size());
	}
	
	@Test
	public void testBuscarLancamentosPorFuncioanarioIdPaginado() {
		PageRequest page  = new PageRequest(0, 10);
		Page<Lancamento> lancamento = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamento.getTotalElements());
	}
	
	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INCIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		return lancamento;
		
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException{
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf("24291173474");
		funcionario.setEmail("email@teste.com");
		funcionario.setEmpresa(empresa);
		return funcionario;
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa exemplo");
		empresa.setCnpj("51463645000100");
		this.empresaRepository.save(empresa);
		return empresa;
	}

}
