// describe('E2E', () => {

//   it('Register ok', () => {

//     cy.visit('/register');

//     cy.get('input[formControlName=firstName]').type('yoga');
//     cy.get('input[formControlName=lastName]').type('studio');
//     cy.get('input[formControlName=email]').type(`yoga${Date.now().toString()}@studio.com`);
//     cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}`);

//     cy.url().should('include', '/login');

//   });

//   it('login ok', () => {

//     cy.visit('/login')
//     cy.get('input[formControlName=email]').type("yoga@studio.com")
//     cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
//     //cy.intercept({method: 'GET',url: '/api/session',},[]).as('session')

//       //! ensure that the user is redirected or navigated to  '/sessions'
//     cy.url().should('include', '/sessions')
//   });

// });



describe('E2E', () => {


  it('Register ok', () => {

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


  it('Login ok', () => {
    cy.url().should('include', '/sessions');
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  });

  it('display sessions ok', () => {
    cy.url().should('include', '/sessions');
    cy.get('.mat-card-title', { multiple: true }).should('have.length', 3); // 2 sessions from the mock + 1 from the "Create new session" card
  });
  it('display account ok', () => {
    cy.get('[routerlink="me"]').click();

    cy.url().should('include', '/me');

    cy.get('p').contains('Name:').should('have.text', 'Name: Admin ADMIN');

    cy.get('.mat-icon').click();
  });
  it('display detail ok', () => {
    cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
    cy.get('button.mat-focus-indicator').eq(1).click();
    cy.url().should('include', '/detail/1');
    cy.get('h1').contains('Dance');
    cy.get('[fxlayout="row"] > .mat-focus-indicator > .mat-button-wrapper > .mat-icon').click();
  });
  it('display edit ok', () => {
    cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
    cy.get('button.mat-focus-indicator').eq(2).click();
    cy.url().should('include', '/update/1');
    cy.get('h1').contains('Update session');
    cy.get('#mat-input-6').should('have.value', 'Dance');
    cy.get('#mat-input-7').clear().type(new Date().toISOString().slice(0, 10));
    cy.get('button[type=submit]').click();
    cy.url().should('include', '/sessions');
  });
  it('display create ok', () => {
    cy.get('.mat-card-title', { multiple: true }).should('have.length', 3);
    cy.get('button.mat-focus-indicator').eq(0).click();
    cy.url().should('include', '/sessions/create');
    cy.get('h1').contains('Create session');
    cy.get('#mat-input-9').clear().type('Session 5');
    cy.get('#mat-input-10').clear().type('2023-04-01');
    cy.get('.mat-form-field-type-mat-select > .mat-form-field-wrapper > .mat-form-field-flex > .mat-form-field-infix').click();
    cy.get('#mat-option-2 > .mat-option-text').click()
    cy.get('#mat-input-11').clear().type('This is a test session.');
    cy.get('button[type=submit]').click();
    cy.url().should('include', '/sessions');
  });
  it('display delete ok', () => {
    cy.get('.mat-card-title', { multiple: true }).should('have.length', 4);
    cy.get('button.mat-focus-indicator').eq(5).click();
    cy.get('h1').contains('Session 5');
    cy.get('button.mat-focus-indicator').eq(1).click();
    cy.url().should('include', '/sessions');
  });
  it('Logout ok', () => {
    cy.get('.mat-toolbar > .ng-star-inserted > :nth-child(3)').click()
    cy.url().should('include', '/');
  });
}); 
