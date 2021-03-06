package br.com.t1tecnologia.finderj.controller;

import br.com.t1tecnologia.finderj.enums.SessionEnum;
import br.com.t1tecnologia.finderj.enums.converter.TipoUsuarioConverter;
import br.com.t1tecnologia.finderj.model.Usuario;
import br.com.t1tecnologia.finderj.repository.UsuarioRepository;
import br.com.t1tecnologia.finderj.util.ConvertToMd5;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author alexandre
 */
@Controller
@RequestMapping(value = {"/usuario", "/login"})
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("usuario/login");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cadastro")
    public ModelAndView cadastro() {
        ModelAndView mvCadastro = new ModelAndView("usuario/cadastro");
        mvCadastro.addObject("tipoUsuarios", new TipoUsuarioConverter().getOptions());
        return mvCadastro;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar")
    public String cadastrar(Usuario u) throws Exception {
        try {
            if (isLoginDisponivel(u.getUsuaLogin())) {
                u.setUsuaSenha(ConvertToMd5.CriptografaSenha(u.getUsuaSenha()));
                u.setUsuaAdmin(false);
                u.setUsuaAtivo(true);
                usuarioRepository.save(u);
                return "true";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "false";
    }

    @ResponseBody
    @RequestMapping(value = "/logar", method = RequestMethod.POST)
    public ModelAndView autenticar(Usuario u, HttpSession session) throws Exception {
        u = usuarioRepository.findByUsuaLoginAndUsuaSenhaAndUsuaAtivoTrue(u.getUsuaLogin(), ConvertToMd5.CriptografaSenha(u.getUsuaSenha()));

        if (u != null) {
            session.setAttribute(SessionEnum.USUARIO.name(), u.getUsuaLogin());
            session.setAttribute(SessionEnum.TIPO_USUARIO.name(), u.getUsuaTipoUsuario());
            session.setAttribute(SessionEnum.ADMIN.name(), u.isUsuaAdmin());

            return new ModelAndView("redirect:/empresa");
        }

        return new ModelAndView("redirect:/usuario");
    }

    private boolean isLoginDisponivel(String usuario) {
        return usuarioRepository.findByUsuaLogin(usuario) == null;
    }

}
