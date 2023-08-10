import UserDto;
import UserRoleDto;
import UserService;
import com.obss.pokedex.library.rest.BaseController;
import com.obss.pokedex.library.rest.MetaResponse;
import com.obss.pokedex.library.rest.PageResponse;
import com.obss.pokedex.library.rest.Response;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService service;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public Response<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        var user = service.createUser(request.toDto());
        return respond(UserResponse.toResponse(user));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PutMapping("/{id}")
    public Response<UserResponse> updateUser(@Valid @PathVariable(name = "id") String id,
                                             @RequestBody UserRequest request) {
        var user = service.updateUser(id, request.toDto());
        return respond(UserResponse.toResponse(user));
    }

    @PutMapping("/my-user")
    public Response<UserResponse> updateMyUser(@Valid @RequestBody UserRequest request) {
        var user = service.updateMyUser(request.toDto());
        return respond(UserResponse.toResponse(user));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}")
    public Response<Void> deleteUser(@PathVariable(name = "id") String id) {
        service.deleteUser(id);
        return new Response<>(MetaResponse.ofSuccess());
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping
    public Response<PageResponse<UserResponse>> getAllUser(Pageable pageable) {
        return respond(toPageResponse(service.getAllUserPageable(pageable)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/username/{userName}")
    public Response<UserResponse> getByUserName(@PathVariable(value = "userName") String userName) {
        return respond(UserResponse.toResponse(service.getByUserName(userName)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/filter")
    public Response<PageResponse<UserResponse>> filterUser(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean activity,
            Pageable pageable) {

        UserDto dto = UserDto.builder()
                .userName(userName)
                .fullName(fullName)
                .activity(activity)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
        return respond(toPageResponse(service.filterUser(dto, pageable)));
    }
    @PostMapping("/catch/{pokemonId}")
    public Response<UserResponse> addPokemonToCatchlist(@PathVariable(value = "pokemonId") String pokemonId) {
        UserDto user = service.addPokemonToCatchlist(pokemonId);
        return respond(UserResponse.toResponse(user));
    }

    @DeleteMapping("/catch/{pokemonId}")
    public Response<UserResponse> deletePokemonFromCatchlist(@PathVariable(value = "pokemonId") String pokemonId) {
        UserDto user = service.deletePokemonFromCatchlist(pokemonId);
        return respond(UserResponse.toResponse(user));
    }

    @PostMapping("/wish/{pokemonId}")
    public Response<UserResponse> addPokemonToWishlist(@PathVariable(value = "pokemonId") String pokemonId) {
        UserDto user = service.addPokemonToWishlist(pokemonId);
        return respond(UserResponse.toResponse(user));
    }

    @DeleteMapping("/wish/{pokemonId}")
    public Response<UserResponse> deletePokemonFromWishlist(@PathVariable(value = "pokemonId") String pokemonId) {
        UserDto user = service.deletePokemonFromWishlist(pokemonId);
        return respond(UserResponse.toResponse(user));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/roles")
    public Response<UserResponse> addRoleToUser(@RequestBody UserRoleDto dto) {
        UserDto user = service.addRoleToUser(dto);
        return respond(UserResponse.toResponse(user));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/roles")
    public Response<UserResponse> removeRoleToUser(@RequestBody UserRoleDto dto) {
        UserDto user = service.removeRoleToUser(dto);
        return respond(UserResponse.toResponse(user));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/{id}")
    public Response<UserResponse> getUserById(@PathVariable(value = "id") String id) {
        return respond(UserResponse.toResponse(service.getUserDtoById(id)));
    }

    private Page<UserResponse> toPageResponse(Page<UserDto> userDtos) {
        return PageUtil.pageToDto(userDtos, UserResponse::toResponse);
    }
}
