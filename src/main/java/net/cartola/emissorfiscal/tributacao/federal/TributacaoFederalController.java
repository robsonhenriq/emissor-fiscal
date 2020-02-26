package net.cartola.emissorfiscal.tributacao.federal;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.cartola.emissorfiscal.documento.Finalidade;
import net.cartola.emissorfiscal.ncm.Ncm;
import net.cartola.emissorfiscal.ncm.NcmService;
import net.cartola.emissorfiscal.operacao.Operacao;
import net.cartola.emissorfiscal.operacao.OperacaoService;
import net.cartola.emissorfiscal.pessoa.RegimeTributario;


@Controller
@RequestMapping("/tributacao-federal")
public class TributacaoFederalController {
	
	@Autowired
	private TributacaoFederalService tributacaoFederalService;
	
	@Autowired
	private OperacaoService operacaoService;
	
	@Autowired
	private NcmService ncmService;
	
	@GetMapping("/cadastro")
	public ModelAndView loadTributacaoFederal() {
		ModelAndView mv = new ModelAndView("tributacao-federal/cadastro");
		TributacaoFederal tributacaoFederal = new TributacaoFederal();
		addObjetosNaView(mv, tributacaoFederal);
		// mv.addObject("textBtnCadastrarEditar", "Cadastrar");
		return mv;
	}
	
	@PostMapping("/cadastro")
	public ModelAndView save(@Valid TributacaoFederal tributacaoFederal, Long operacaoId, Long ncmId, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			ModelAndView mv = new ModelAndView("tributacao-federal/cadastro");
			addObjetosNaView(mv, tributacaoFederal);
//			mv.addObject("mensagemErro", tributacaoFederalService.getMensagensErros(result, existeNumeroEExecao));
			return mv;
		}
		ModelAndView mv = new ModelAndView("redirect:/tributacao-federal/cadastro");
		
		try {
			Operacao operacao = operacaoService.findOne(operacaoId).get();
			Ncm ncm = ncmService.findOne(ncmId).get();
			
			tributacaoFederal.setOperacao(operacao);
			tributacaoFederal.setNcm(ncm);
			
			tributacaoFederalService.save(tributacaoFederal);
		} catch (Exception ex) {
			mv.setViewName("tributacao-federal/cadastro");
			addObjetosNaView(mv, tributacaoFederal);
			mv.addObject("mensagemErro", "Houve algum erro ao tentar cadastrar essa TRIBUTAÇÃO FEDERAL!! ");
		}
		
		attributes.addFlashAttribute("mensagemSucesso", "Tributação Federal alterado/cadastrado com sucesso!");
		return mv;
	}
		
	@GetMapping("/consulta")
	public ModelAndView findAll() {
		ModelAndView mv = new ModelAndView("tributacao-federal/consulta");
		List<TributacaoFederal> listTributacaoFederal = tributacaoFederalService.findAll();
		
		if (!listTributacaoFederal.isEmpty()) {
			listTributacaoFederal.forEach(tributacaoFederal -> {
				tributacaoFederalService.multiplicaTributacaoFederalPorCem(tributacaoFederal);
			});
		}
		mv.addObject("listTributacaoFederal", listTributacaoFederal);
		
		return mv;
	}

	@PostMapping("/consulta")
	public ModelAndView findByNumero(@RequestParam("ncm") String numeroNcm, Model model) {
		ModelAndView mv = new ModelAndView("tributacao-federal/consulta");
		try {
			List<Ncm> listNcm = ncmService.findByNumero(Integer.parseInt(numeroNcm));
			List<TributacaoFederal> listTributacaoFederal = tributacaoFederalService.findTributacaoFederalByVariosNcms(listNcm);
			
			if (!listTributacaoFederal.isEmpty()) {
				listTributacaoFederal.forEach(tributacaoFederal -> {
					tributacaoFederalService.multiplicaTributacaoFederalPorCem(tributacaoFederal );
				});
			}
			mv.addObject("listTributacaoFederal", listTributacaoFederal);
		} catch (Exception ex) {
			mv.addObject("mensagemErro", "Erro ao tentar buscar a tributação federal pelo NCM informado");
		} 
		return mv;
	}
	
	// Método que irá carregar na tela de cadastro, os valores cadastrados de uma tributação federal(para poder editar)
	@GetMapping("/editar/{id}")
	public ModelAndView edit(@PathVariable long id, Model model) {
		ModelAndView mv = new ModelAndView("tributacao-federal/cadastro");
		TributacaoFederal tributacaoFederal = tributacaoFederalService.findOne(id).get();
		tributacaoFederalService.multiplicaTributacaoFederalPorCem(tributacaoFederal);

		model.addAttribute("operacaoIdSelecionado", tributacaoFederal.getOperacao().getId());
		model.addAttribute("ncmdIdSelecionado", tributacaoFederal.getNcm().getId());
		model.addAttribute("finalidadeSelecionado", tributacaoFederal.getFinalidade());
		model.addAttribute("regimeTributarioSelecionado", tributacaoFederal.getRegimeTributario());
		
		addObjetosNaView(mv, tributacaoFederal);
		// mv.addObject("textBtnCadastrarEditar", "Editar");
		return mv;
	}

//	@PostMapping("/deletar/{id}")
//	public ModelAndView delete(@PathVariable("id") long id, RedirectAttributes attributes, Model model) {
//		try {
//
//			tributacaoFederalService.deleteById(id);
//		} catch (Exception ex) {
//			model.addAttribute("mensagemErro", "Erro ao tentar deletar a tributação estadual de ID: " + id);
//		}
//		attributes.addFlashAttribute("mensagemSucesso", "Tributação Estadual deletado com sucesso!");
//		return new ModelAndView("redirect:/tributacaoEstadual/consulta");
//	}
	
	private void addObjetosNaView(ModelAndView mv, TributacaoFederal tributacaoFederal) {
		mv.addObject("tributacaoFederal", tributacaoFederal);
		mv.addObject("listOperacao", operacaoService.findAll());
		mv.addObject("listNcms", ncmService.findAll());
		mv.addObject("listFinalidade", Arrays.asList(Finalidade.values()));
		mv.addObject("listRegimeTributario", Arrays.asList(RegimeTributario.values()));
	}

}
