package br.com.robertocarneiro.pautaapi.restclients;

import br.com.robertocarneiro.pautaapi.dtos.UserByCpfDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "userServiceClient", url = "${client.user.url}")
public interface UserServiceClient {

	@GetMapping(value = "/{cpf}")
	UserByCpfDTO findUserByCpf(@PathVariable("cpf") String cpf);

}
