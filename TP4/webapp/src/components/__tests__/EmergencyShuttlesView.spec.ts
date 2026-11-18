import { describe, expect, test, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import axios from 'axios'
import EmergencyShuttlesView from '../EmergencyShuttlesView.vue'

vi.mock('axios')

describe('EmergencyShuttlesView', () => {
    const ANY_COST = 150000
    const ANY_SHUTTLE_TYPE = "RESCUE_SHIP"
    const ANY_TRAVELER_ID = "395e7715-74fc-468a-9795-1adc79e6148a"
    const ANY_TRAVELER_NAME = "John Doe"
    const ANY_CREW_MEMBER_ID = "ABC123"
    const ANY_CREW_MEMBER_NAME = "Jane Doe"

    function createWrapper() {
        return mount(EmergencyShuttlesView)
    }

    function givenAxiosGetResolvesWithShuttles() {
        const emergencyShuttles = [
            {
                type: ANY_SHUTTLE_TYPE,
                travelers: [
                    {
                        travelerId: ANY_TRAVELER_ID,
                        travelerName: ANY_TRAVELER_NAME,
                    }
                ],
                crewMembers: [
                    {
                        employeeId: ANY_CREW_MEMBER_ID,
                        crewMemberName: ANY_CREW_MEMBER_NAME,
                    }
                ]
            }
        ]

        ;(axios.get as unknown as vi.Mock).mockResolvedValueOnce({
            data: {
                cost: ANY_COST,
                emergencyShuttles: emergencyShuttles,
            }
        })
    }

    function givenAxiosGetResolvesWithEmptyManifest() {
        ;(axios.get as unknown as vi.Mock).mockResolvedValueOnce({
            data: {
                cost: ANY_COST,
                emergencyShuttles: []
            }
        })
    }

    async function whenUserRequestsEmergencyShuttlesManifest(wrapper: ReturnType<typeof createWrapper>) {
        await wrapper.find('[data-cy="fetch-emergency-shuttles-manifest"]').trigger('click')

        await flushPromises()
    }

    test("given shuttles returned by the api when user requests emergency shuttles manifest then emergency shuttles table should be displayed", async () => {
        const wrapper = createWrapper()

        givenAxiosGetResolvesWithShuttles()

        await whenUserRequestsEmergencyShuttlesManifest(wrapper)

        const cost = wrapper.find('[data-cy="total-cost"]').text()
        const shuttles = wrapper.findAll('[data-cy="emergency-shuttle"]')
        const travelerIds = shuttles[0].findAll('.traveler-id-class')
        const travelerNames = shuttles[0].findAll('.traveler-name-class')
        const employeeIds = shuttles[0].findAll('.employee-id-class')
        const crewMemberNames = shuttles[0].findAll('.crew-member-name-class')
        const shuttleType = shuttles[0].find('.shuttle-type-class').text()

        expect(shuttles).toHaveLength(1)
        expect(travelerIds).toHaveLength(1)
        expect(travelerNames).toHaveLength(1)
        expect(employeeIds).toHaveLength(1)
        expect(crewMemberNames).toHaveLength(1)
        expect(cost).toBe(String(ANY_COST))
        expect(shuttleType).toBe(ANY_SHUTTLE_TYPE)
        expect(travelerIds[0].text()).toBe(ANY_TRAVELER_ID)
        expect(travelerNames[0].text()).toBe(ANY_TRAVELER_NAME)
        expect(employeeIds[0].text()).toBe(ANY_CREW_MEMBER_ID)
        expect(crewMemberNames[0].text()).toBe(ANY_CREW_MEMBER_NAME)
    })

    test("given empty manifest returned by the api when user requests emergency shuttles manifest then should not display emergency shuttles table", async () => {
        const wrapper = createWrapper()

        givenAxiosGetResolvesWithEmptyManifest()

        await whenUserRequestsEmergencyShuttlesManifest(wrapper)

        const shuttlesTable = wrapper.find('table')

        expect(shuttlesTable.exists()).toBe(false)
    })

    test("given empty manifest returned by the api when user requests emergency shuttles manifest then should switch button color to red", async () => {
        const wrapper = createWrapper()

        givenAxiosGetResolvesWithEmptyManifest()

        await whenUserRequestsEmergencyShuttlesManifest(wrapper)

        const button = wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]')

        expect(button.classes()).toContain('red-button')
    })
})