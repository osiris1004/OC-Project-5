describe('E2E', () => {

  it('Register ok', () => {

    cy.visit('/register');

    cy.get('input[formControlName=firstName]').type('yoga');
    cy.get('input[formControlName=lastName]').type('studio');
    cy.get('input[formControlName=email]').type(`yoga${Date.now().toString()}@studio.com`);
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}`);

    cy.url().should('include', '/login');

  });

  it('login ok', () => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      //! the respond object containing mocked response body
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    //cy.intercept({method: 'GET',url: '/api/session',},[]).as('session')

      //! ensure that the user is redirected or navigated to  '/sessions'
    cy.url().should('include', '/sessions')
  });

});


