describe('All spec', () => {
    beforeEach(() => {
        cy.login('yoga@studio.com', 'test!1234');
    });
    it('Not Found successfully', () => {
        cy.visit('/random');
        cy.url().should('include', '/404');
    });
  it('Register successfully', () => {
        
        cy.visit('/register');

        cy.intercept(
        {
            method: 'GET',
            url: '/api/session',
        },
        []
        ).as('session');

        cy.get('input[formControlName=firstName]').type('yoga');
        cy.get('input[formControlName=lastName]').type('studio');
        cy.get('input[formControlName=email]').type(
          `yoga${Date.now().toString()}@studio.com`
        );
        cy.get('input[formControlName=password]').type(
        `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('include', '/login');
    });
    it('Login successfully', () => {
        cy.url().should('include', '/sessions');
    });
    it('Logout successfully', () => {
      cy.get('#logout').click();

      cy.url().should('include', '/');
    });
    it('Show sessions successfully', () => {
      cy.url().should('include', '/sessions');
      cy.get('.mat-card-title', { multiple: true }).should('have.length', 3); // 2 sessions from the mock + 1 from the "Create new session" card
    });
    it('See account info successfully', () => {
      cy.get('#account').click();

      cy.url().should('include', '/me');

      cy.get('p').contains('Name:').should('have.text', 'Name: Admin ADMIN');
    });
    it('Show detail successfully', () => {
      cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
      cy.get('button.mat-focus-indicator').eq(1).click();
      cy.url().should('include', '/detail/1');
      cy.get('h1').contains('Dance');
    });
    it('Show edit successfully', () => {
      cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
      cy.get('button.mat-focus-indicator').eq(2).click();
      cy.url().should('include', '/update/1');
      cy.get('h1').contains('Update session');
      cy.get('#mat-input-2').should('have.value', 'Dance');
      cy.get('#mat-input-2').clear().type('Dance');
      cy.get('button[type=submit]').click();
      cy.url().should('include', '/sessions');
    });
    it('Show create successfully', () => {
      cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
      cy.get('button.mat-focus-indicator').eq(0).click();
      cy.url().should('include', '/sessions/create');
      cy.get('h1').contains('Create session');
      cy.get('#mat-input-2').clear().type('Session 5');
      cy.get('#mat-input-3').clear().type('2023-04-01');
      cy.get('#mat-select-0').click();
      cy.get('#mat-option-1').click();
      cy.get('#mat-input-4').clear().type('This is a test session.');
      cy.get('button[type=submit]').click();
      cy.url().should('include', '/sessions');
    });
    it('Show delete successfully', () => {
      cy.get('.mat-card-title', { multiple: true }).should('have.length', 4);
      cy.get('button.mat-focus-indicator').eq(5).click();
      cy.get('h1').contains('Session 5');
      cy.get('button.mat-focus-indicator').eq(1).click();
      cy.url().should('include', '/sessions');
    });
});
