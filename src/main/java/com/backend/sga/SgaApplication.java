package com.backend.sga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.sga.model.TipoUsuario;
import com.backend.sga.model.Usuario;
import com.backend.sga.repository.UsuarioRepository;

//estamos desabilitando os endpoints para que cada alteração não tenha que ficar fazendo login
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableFeignClients
public class SgaApplication implements CommandLineRunner {
    @Autowired UsuarioRepository userRepository;
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    public static void main(String[] args) {
        SpringApplication.run(SgaApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        Usuario suporte = new Usuario();
        Usuario AdmfromDb = userRepository.findByNif("SGADM2022");
        String crip = this.encoder.encode("123");
        if(AdmfromDb == null) {
            suporte.setEmail("sga.suporte@gmail.com");
            suporte.setAtivo(true);
            suporte.setNif("SGADM2022");
            suporte.setTipo(TipoUsuario.ADMINISTRADOR);
            suporte.setNome("Administrador");
            suporte.setSenha(crip);     
            userRepository.save(suporte);
        }
    }

}
