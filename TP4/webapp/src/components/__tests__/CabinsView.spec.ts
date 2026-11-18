import { describe, expect, test, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import axios from 'axios'
import CabinsView from '../cabinsView.vue'

vi.mock('axios')

describe('CabinsView', () => {
  const ANY_CABIN_ID_FIRST = 'A1'
  const ANY_CABIN_ID_SECOND = 'A2'
  const ANY_STANDARD_CATEGORY = 'STANDARD'
  const ANY_BOOKING_ID_FIRST = 'booking-id-1'
  const ANY_BOOKING_ID_SECOND = 'booking-id-2'

  function createWrapper() {
    return mount(CabinsView)
  }

  function givenAxiosGetResolvesWithCabins() {
    const cabins = [
      {
        cabinId: ANY_CABIN_ID_FIRST,
        category: ANY_STANDARD_CATEGORY,
        bookingId: ANY_BOOKING_ID_FIRST
      },
      {
        cabinId: ANY_CABIN_ID_SECOND,
        category: ANY_STANDARD_CATEGORY,
        bookingId: ANY_BOOKING_ID_SECOND
      }
    ]

    ;(axios.get as unknown as vi.Mock).mockResolvedValueOnce({
      data: {
        cabins
      }
    })
  }

  function givenAxiosGetResolvesWithEmptyCabins() {
    ;(axios.get as unknown as vi.Mock).mockResolvedValueOnce({
      data: {
        cabins: []
      }
    })
  }

  function givenAxiosGetRejects() {
    ;(axios.get as unknown as vi.Mock).mockRejectedValueOnce(new Error('any error'))
  }

  async function whenUserRequestsCabinAttributions(wrapper: ReturnType<typeof createWrapper>) {
    await wrapper.find('[data-cy="fetch-cabin-attribution"]').trigger('click')

    await flushPromises()
  }

  test('given cabins returned by api when user requests cabin attributions then cabins table is displayed in received order', async () => {
    const wrapper = createWrapper()

    givenAxiosGetResolvesWithCabins()

    await whenUserRequestsCabinAttributions(wrapper)

    const rows = wrapper.findAll('[data-cy="cabin-attribution-details"]')
    const firstRowCabinId = rows[0].find('.cabin-id-item').text()
    const secondRowCabinId = rows[1].find('.cabin-id-item').text()

    expect(rows).toHaveLength(2)
    expect(firstRowCabinId).toBe(ANY_CABIN_ID_FIRST)
    expect(secondRowCabinId).toBe(ANY_CABIN_ID_SECOND)
  })

  test('given no cabins returned by api when user requests cabin attributions then cabins table is not displayed', async () => {
    const wrapper = createWrapper()

    givenAxiosGetResolvesWithEmptyCabins()

    await whenUserRequestsCabinAttributions(wrapper)

    const cabinsTable = wrapper.find('table')

    expect(cabinsTable.exists()).toBe(false)
  })

  test('given api call fails when user requests cabin attributions then cabins remain empty', async () => {
    const wrapper = createWrapper()

    givenAxiosGetRejects()

    await whenUserRequestsCabinAttributions(wrapper)

    const rows = wrapper.findAll('[data-cy="cabin-attribution-details"]')

    expect(rows).toHaveLength(0)
  })
})
