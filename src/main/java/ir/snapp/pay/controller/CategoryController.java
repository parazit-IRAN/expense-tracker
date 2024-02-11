package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.CategoryInputDto;
import ir.snapp.pay.dto.CategoryOutputDto;
import ir.snapp.pay.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/categories")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Category", description = "The Category API. Contains all the operations that can be performed on a category.")
public class CategoryController extends BaseController {

	private final CategoryService categoryService;


	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "create a category")
	public ResponseEntity<CategoryOutputDto> createCategory(@Valid @RequestBody CategoryInputDto categoryInputDto,
															Authentication authentication) {
		log.debug("REST request to save Category : {}, User Email: {}", categoryInputDto, authentication.getName());
		try {
			CategoryOutputDto categoryOutputDto = categoryService.createCategory(categoryInputDto, authentication.getName());
			return success(categoryOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get all categories for current user")
	public ResponseEntity<List<CategoryOutputDto>> getAllCategory(Authentication authentication) {
		log.debug("REST request to get all Category , User Email: {}", authentication.getName());
		try {
			List<CategoryOutputDto> categories = categoryService.getAllCategory(authentication.getName());
			return success(categories);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get a categories by id")
	public ResponseEntity<CategoryOutputDto> getCategory(@PathVariable("id") Long id, Authentication authentication) {
		log.debug("REST request to get a Category id : {}, User Email: {}", id, authentication.getName());
		try {
			CategoryOutputDto categoryOutputDto = categoryService.getCategory(id, authentication.getName());
			return success(categoryOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "delete a category")
	public ResponseEntity<String> deleteCategory(@Valid @PathVariable("id") Long categoryId, Authentication authentication) {
		log.debug("REST request to delete Category id: {}, User Email :{}", categoryId, authentication.getName());
		try {
			categoryService.deleteCategory(categoryId, authentication.getName());
			return success("Deleted Category id: " + categoryId);
		} catch (Exception e) {
			return failure(e);
		}
	}

}
