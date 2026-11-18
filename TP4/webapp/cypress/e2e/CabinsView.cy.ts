describe('Cabin Attributions View', () => {
  const cruiseId = 'JUPITER_MOONS_EXPLORATION_2085'
  const cabinsEndpoint = `/cruises/${cruiseId}/cabins`

  it('affiche un message quand aucune cabine nest attribuee', () => {
    cy.intercept('GET', `${cabinsEndpoint}*`, {
      statusCode: 200,
      body: { cabins: [] }
    }).as('getCabinsEmpty')

    cy.visit('/')

    cy.get('[data-cy="fetch-cabin-attribution"]').click()

    cy.wait('@getCabinsEmpty')

    cy.get('[data-cy="cabin-attribution-details"]').should('not.exist')
    cy.get('[data-cy="no-cabin-attributions-message"]').should('be.visible')
  })

  it('affiche les cabines attribuees quand il y en a', () => {
    const responseBody = {
      cabins: [
        { cabinId: 'C1', category: 'ECONOMY', bookingId: 'B1' },
        { cabinId: 'C2', category: 'LUXURY', bookingId: 'B2' }
      ]
    }

    cy.intercept('GET', `${cabinsEndpoint}*`, {
      statusCode: 200,
      body: responseBody
    }).as('getCabins')

    cy.visit('/')

    cy.get('[data-cy="fetch-cabin-attribution"]').click()

    cy.wait('@getCabins')

    cy.get('[data-cy="cabin-attribution-details"]').should('have.length', responseBody.cabins.length)

    cy.get('[data-cy="cabin-attribution-details"]').first().within(() => {
      cy.get('.cabin-id-item').should('contain', 'C1')
      cy.get('.cabin-category-item').should('contain', 'ECONOMY')
      cy.get('.cabin-bookingId-item').should('contain', 'B1')
    })
  })
})
