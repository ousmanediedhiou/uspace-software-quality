describe("Emergency Shuttles View", () => {
    const cruiseId = 'JUPITER_MOONS_EXPLORATION_2085'
    const emergencyShuttlesEndpoint = `/cruises/${cruiseId}/emergencyShuttles`

    it("le bouton get manifest devient rouge quand un manifeste vide est obtenu et aucun tableau n est affiche", () => {
        cy.intercept('GET', `${emergencyShuttlesEndpoint}*`, {
            statusCode: 200,
            body: { cost: 0, emergencyShuttles: [] }
        }).as('getManifestEmpty')

        cy.visit('/')

        cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click()

        cy.wait('@getManifestEmpty')

        cy.get('[data-cy="manifest-details"]').should('not.exist')
        cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').should('have.class', 'red-button')
    })

    it("affiche les navettes d urgence quand il y en a", () => {
        const responseBody = {
            cost: 150000,
            emergencyShuttles: [
                {
                    type: "RESCUE_SHIP",
                    travelers: [
                        {
                            travelerId: "395e7715-74fc-468a-9795-1adc79e6148a",
                            travelerName: "John Doe",
                        }
                    ],
                    crewMembers: [
                        {
                            employeeId: "ABC123",
                            crewMemberName: "Jane Doe",
                        }
                    ]
                }
            ]
        }

        cy.intercept('GET', `${emergencyShuttlesEndpoint}*`, {
            statusCode: 200,
            body: responseBody
        }).as('getEmergencyShuttlesManifest')

        cy.visit('/')

        cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click()

        cy.wait('@getEmergencyShuttlesManifest')

        cy.get('[data-cy="total-cost"]').should('contain', 150000)
        cy.get('[data-cy="emergency-shuttle"]').first().within(() => {
            cy.get('.shuttle-type-class').should('contain', 'RESCUE_SHIP')

            cy.get('#traveler-395e7715-74fc-468a-9795-1adc79e6148a').within(() => {
                cy.get('.traveler-id-class').should('contain', '395e7715-74fc-468a-9795-1adc79e6148a')
                cy.get('.traveler-name-class').should('contain', 'John Doe')
            })

            cy.get('#crewMember-ABC123').within(() => {
                cy.get('.employee-id-class').should('contain', 'ABC123')
                cy.get('.crew-member-name-class').should('contain', 'Jane Doe')
            })
        })
    })
})