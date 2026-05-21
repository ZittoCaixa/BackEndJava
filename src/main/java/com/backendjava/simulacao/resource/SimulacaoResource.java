package com.backendjava.simulacao.resource;

import com.backendjava.simulacao.dto.ErrorResponse;
import com.backendjava.simulacao.dto.SimulacaoRequest;
import com.backendjava.simulacao.dto.SimulacaoResponse;
import com.backendjava.simulacao.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulacoes")
public class SimulacaoResource {

    private final SimulacaoService simulacaoService;

    @Inject
    public SimulacaoResource(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @POST
    @Operation(summary = "Simula juros compostos e persiste historico")
    @APIResponse(responseCode = "201", description = "Simulacao criada")
    @APIResponse(responseCode = "400", description = "Payload invalido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Response criar(@Valid SimulacaoRequest request) {
        SimulacaoResponse response = simulacaoService.simular(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consulta simulacao por id")
    @APIResponse(responseCode = "200", description = "Simulacao encontrada")
    @APIResponse(responseCode = "404", description = "Simulacao nao encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public SimulacaoResponse buscar(@PathParam("id") Long id) {
        return simulacaoService.buscarPorId(id);
    }
}

