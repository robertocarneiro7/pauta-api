package br.com.robertocarneiro.pautaapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteVersaoController {

    @Operation(summary = "Recurso Vers�o 0",
            description = "Recurso depreceado para testar o versionamento")
    @Deprecated
    @GetMapping(value = "/v0${controller.teste-versao.path}")
    public String testVersionV0() {
        return "Recurso depreceado. Vers�o: v0";
    }

    @Operation(summary = "Recurso Vers�o 1", description = "Recurso mais novo para testar o versionamento")
    @GetMapping(value = {"${controller.teste-versao.path}", "/v1${controller.teste-versao.path}"})
    public String testVersionV1() {
        return "Recurso da vers�o mais nova. Vers�o: v1";
    }

}
