package br.com.robertocarneiro.pautaapi.aspects;

import br.com.robertocarneiro.pautaapi.exceptions.DefaultException;
import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import feign.FeignException;
import feign.Request;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class OpenFeignAspect {

    @Value("${client.user.url}")
    private String clientUserUrl;

    @Around("execution(* br.com.robertocarneiro.pautaapi.restclients.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object objectToReturn;
        try {
            objectToReturn = joinPoint.proceed();
            return objectToReturn;
        } catch (FeignException feignException) {
            throw Optional
                    .of(feignException)
                    .filter(ex -> ex instanceof FeignException.NotFound)
                    .map(FeignException::request)
                    .map(Request::url)
                    .map(url -> url.replace(clientUserUrl + "/", ""))
                    .map(cpf -> new DefaultException(MessageUtil.get("message.error.cpf-not-found", cpf),
                            HttpStatus.NOT_FOUND))
                    .orElse(new DefaultException(feignException.contentUTF8(), HttpStatus.FAILED_DEPENDENCY));
        }

    }
}
