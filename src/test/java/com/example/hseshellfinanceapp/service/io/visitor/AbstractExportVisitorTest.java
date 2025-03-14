package com.example.hseshellfinanceapp.service.io.visitor;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractExportVisitorTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private TestExportVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new TestExportVisitor(bankAccountFacade, categoryFacade, operationFacade);
    }

    @Test
    void exportData_shouldCallCorrectMethods() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(bankAccountFacade.getAllAccounts()).thenReturn(Collections.emptyList());
        when(categoryFacade.getAllCategories()).thenReturn(Collections.emptyList());
        when(operationFacade.getAllOperations()).thenReturn(Collections.emptyList());

        // When
        visitor.exportData(outputStream, true, true, true);

        // Then
        verify(bankAccountFacade).getAllAccounts();
        verify(categoryFacade).getAllCategories();
        verify(operationFacade).getAllOperations();
        verify(visitor.spy).startExport(outputStream);
        verify(visitor.spy).visitAccounts(any(), eq(outputStream));
        verify(visitor.spy).visitCategories(any(), eq(outputStream));
        verify(visitor.spy).visitOperations(any(), eq(outputStream));
        verify(visitor.spy).endExport(outputStream);
    }

    @Test
    void exportData_withNoAccounts_shouldNotVisitAccounts() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        visitor.exportData(outputStream, false, true, true);

        // Then
        verify(bankAccountFacade, never()).getAllAccounts();
        verify(visitor.spy, never()).visitAccounts(any(), any());
        verify(visitor.spy).visitCategories(any(), any());
        verify(visitor.spy).visitOperations(any(), any());
    }

    // A test implementation that tracks method calls
    private static class TestExportVisitor extends AbstractExportVisitor {
        // Spy to verify method calls
        private final AbstractExportVisitor spy;

        public TestExportVisitor(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade,
                                 OperationFacade operationFacade) {
            super(bankAccountFacade, categoryFacade, operationFacade);
            this.spy = spy(this);
        }

        @Override
        protected void startExport(java.io.OutputStream outputStream) {
        }

        @Override
        protected void visitAccounts(List<BankAccount> accounts, java.io.OutputStream outputStream) {
        }

        @Override
        protected void visitCategories(List<Category> categories, java.io.OutputStream outputStream) {
        }

        @Override
        protected void visitOperations(List<Operation> operations, java.io.OutputStream outputStream) {
        }

        @Override
        protected void endExport(java.io.OutputStream outputStream) {
        }
    }
}
