package com.backend.portfolio.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.TechnologyRequestInsert;
import com.backend.portfolio.models.responses.TechnologyResponse;
import com.backend.portfolio.models.states.enums.Proficiency;
import com.backend.portfolio.models.updates.TechnologyRequestUpdate;
import com.backend.portfolio.repositories.TechnologyRepository;

@ExtendWith(MockitoExtension.class)
class TechnologyServiceTest {

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private TechnologyService technologyService;

    private Technology testTechnology;
    private TechnologyRequestInsert testRequestInsert;
    private TechnologyRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testTechnology = Technology.builder()
                .id(1L)
                .name("Java")
                .category("Backend")
                .type("Programming Language")
                .proficiency(Proficiency.EXPERT)
                .banner("java.png")
                .github("github.com/java")
                .build();

        testRequestInsert = TechnologyRequestInsert.builder()
                .name("Java")
                .category("Backend")
                .type("Programming Language")
                .proficiency(Proficiency.EXPERT)
                .banner("java.png")
                .github("github.com/java")
                .build();

        testRequestUpdate = TechnologyRequestUpdate.builder()
                .name("Java 21")
                .proficiency(Proficiency.ADVANCED)
                .build();
    }

    @Test
    void getAllTechnologies_ShouldReturnListOfTechnologies() {
        // Arrange
        List<Technology> technologies = Arrays.asList(testTechnology);
        when(technologyRepository.findAllByOrderByCreatedAtDesc()).thenReturn(technologies);

        // Act
        List<TechnologyResponse> result = technologyService.getAllTechnologies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(technologyRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllTechnologiesByCategory_ShouldReturnFilteredList() {
        // Arrange
        List<Technology> technologies = Arrays.asList(testTechnology);
        when(technologyRepository.findAllByCategoryOrderByNameAsc("Backend")).thenReturn(technologies);

        // Act
        List<TechnologyResponse> result = technologyService.getAllTechnologiesByCategory("Backend");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(technologyRepository, times(1)).findAllByCategoryOrderByNameAsc("Backend");
    }

    @Test
    void getTechnologyById_WhenExists_ShouldReturnTechnology() {
        // Arrange
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));

        // Act
        TechnologyResponse result = technologyService.getTechnologyById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testTechnology.getName(), result.getName());
        verify(technologyRepository, times(1)).findById(1L);
    }

    @Test
    void getTechnologyById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(technologyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            technologyService.getTechnologyById(999L));
        verify(technologyRepository, times(1)).findById(999L);
    }

    @Test
    void getTechnologyByName_WhenExists_ShouldReturnTechnology() {
        // Arrange
        when(technologyRepository.findByName("Java")).thenReturn(Optional.of(testTechnology));

        // Act
        TechnologyResponse result = technologyService.getTechnologyByName("Java");

        // Assert
        assertNotNull(result);
        assertEquals(testTechnology.getName(), result.getName());
        verify(technologyRepository, times(1)).findByName("Java");
    }

    @Test
    void getTechnologyByName_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(technologyRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            technologyService.getTechnologyByName("Unknown"));
        verify(technologyRepository, times(1)).findByName("Unknown");
    }

    @Test
    void getAllTechnologiesByProfileId_ShouldReturnListOfTechnologies() {
        // Arrange
        List<Technology> technologies = Arrays.asList(testTechnology);
        when(technologyRepository.findAllByProfileIdOrderByCategoryAndName(1L)).thenReturn(technologies);

        // Act
        List<TechnologyResponse> result = technologyService.getAllTechnologiesByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(technologyRepository, times(1)).findAllByProfileIdOrderByCategoryAndName(1L);
    }

    @Test
    void createTechnology_WithValidData_ShouldReturnCreatedTechnology() {
        // Arrange
        when(technologyRepository.save(any(Technology.class))).thenReturn(testTechnology);

        // Act
        TechnologyResponse result = technologyService.createTechnology(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testTechnology.getName(), result.getName());
        verify(technologyRepository, times(1)).save(any(Technology.class));
    }

    @Test
    void updateTechnology_WithValidData_ShouldReturnUpdatedTechnology() {
        // Arrange
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));
        when(technologyRepository.save(any(Technology.class))).thenReturn(testTechnology);

        // Act
        TechnologyResponse result = technologyService.updateTechnology(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(technologyRepository, times(1)).findById(1L);
        verify(technologyRepository, times(1)).save(any(Technology.class));
    }

    @Test
    void updateTechnology_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(technologyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            technologyService.updateTechnology(999L, testRequestUpdate));
        verify(technologyRepository, times(1)).findById(999L);
        verify(technologyRepository, never()).save(any(Technology.class));
    }

    @Test
    void deleteTechnology_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(technologyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(technologyRepository).deleteById(1L);

        // Act
        technologyService.deleteTechnology(1L);

        // Assert
        verify(technologyRepository, times(1)).existsById(1L);
        verify(technologyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTechnology_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(technologyRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            technologyService.deleteTechnology(999L));
        verify(technologyRepository, times(1)).existsById(999L);
        verify(technologyRepository, never()).deleteById(anyLong());
    }
}

