package usf.controller.usuario;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import usf.model.basic.ModelBasic;
import usf.model.usuario.Usuario;
import usf.model.usuario.UsuarioDAO;

public class UsuarioServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = null;
	
	public UsuarioServlet() {
		super();
	}
	
	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		String jdbcDriver = getServletContext().getInitParameter("jdbcDriver");
		
		usuarioDAO = new UsuarioDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDriver);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String action = ((HttpServletRequest) request).getPathInfo();
		//String action = request.getServletPath();
		
		if (action == null) {
			return;
		}
		
		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertUsuario(request, response);
				break;
			case "/delete":
				deleteUsuario(request, response);
				break;
			case "/edit":
				showEditUsuario(request, response);
				break;
			case "/update":
				updateUsuario(request, response);
				break;
			case "/list" :
				listUsuario(request, response);
				break;	
			default:
				listUsuario(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
		
	private void listUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		try {
			List<ModelBasic> listUsuario = usuarioDAO.listAll();
			request.setAttribute("listUsuario", listUsuario);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/app/usuario/UsuarioList.jsp");
			dispatcher.forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/app/usuario/UsuarioForm.jsp");
			dispatcher.forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void showEditUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			ModelBasic existingUsuario = usuarioDAO.getRecord(id);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/app/usuario/UsuarioForm.jsp");
			request.setAttribute("usuario", existingUsuario);
			dispatcher.forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void insertUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		try {
			String nome = request.getParameter("nome");
			String telefone = request.getParameter("telefone");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nivelAcesso = request.getParameter("nivelAcesso");
			
			Usuario newUsuario = new Usuario(nome, telefone, email, login, senha, nivelAcesso);
			usuarioDAO.insert(newUsuario);
			response.sendRedirect("list");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void updateUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String nome = request.getParameter("nome");
			String telefone = request.getParameter("telefone");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nivelAcesso = request.getParameter("nivelAcesso");
			
			Usuario usuario = new Usuario(id, nome, telefone, email, login, senha, nivelAcesso);
			usuarioDAO.update(usuario);
			response.sendRedirect("list");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void deleteUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Usuario usuario = new Usuario(id);
			usuarioDAO.delete(usuario);
			response.sendRedirect("list");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}