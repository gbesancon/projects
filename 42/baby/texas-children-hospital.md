Convert to state diagram

``` mermaid
graph TB
    home[Home]

    home --> |pre-register| pre-registered(Pre-registered)
    pre-registered --> hospital

    home --> |prepare bag| bag-prepared(Bag prepared)
    bag-prepared --> hospital

    home --> |install car seat| car-seat-installed(Car seat installed)
    car-seat-installed --> hospital
    
    home --> |go to hospital| hospital(Hospital - https://women.texaschildren.org)
    hospital --> parking{Which parking ?}
    parking --> |13$ / 4h| self-parking[Self Parking]
    parking --> |13$ / day| valet-parking[Valet parking]
    self-parking --> screening[Screening - Ground floor]
    valet-parking --> screening
    screening --> |receive bracelets| women-assessment-center(Women Assessment Center - 11th floor - max 1 adult)
    women-assessment-center --> wac-private-room(Private Room)
    wac-private-room --> ready-to-go-out{Baby ready to go out ?}
    ready-to-go-out --> |no| home
    ready-to-go-out --> |yes| covid-19-vaccinated{COVID-19 vaccinated ?}
    covid-19-vaccinated --> |no| pcr-test(PCR test)
    pcr-test --> |positive| pcr-test-positive(?)
    pcr-test --> |negative| family-birth-center
    covid-19-vaccinated --> |yes| family-birth-center
    
    home --> |define birth plan| birth-plan(Birth plan)
    birth-plan --> family-birth-center

    family-birth-center(Family Birth Center - 9th - max 2 adults)

    family-birth-center --> type-of-birth-planned{Type of birth planned ?}
    type-of-birth-planned --> |Vaginal delivery| vaginal-delivery[Vaginal delivery: provider, nurse, baby nurse, technologist]
    vaginal-delivery --> vaginal-delivery-complication(Vaginal delivery complication ?)
    vaginal-delivery-complication --> |no| burnex(Burnex - skin to skin 1h)
    vaginal-delivery-complication --> |emergency C-section - no company| womens-surgery
    
    type-of-birth-planned --> |schedule C-section - max 1 adult| womens-surgery
    
    type-of-birth-planned --> |unschedule C-section - max 1 adult| womens-surgery(Women's surgery - 5th floor)
    womens-surgery --> surgery-delivery(C-section delivery - 10 minutes)
    surgery-delivery --> burnex

    burnex --> post-birth-recovery(Recovery for 2h)

    post-birth-recovery --> post-birth-food(Post birth food)

    post-birth-food --> mother-baby-unit(Mother Baby Unit - 12th & 14th floor)

    mother-baby-unit --> mbu-type-of-room{Type of room ?}

    mbu-type-of-room --> |suite - 750$ / stay| mbu-type-of-birth{Type of birth ?}

    mbu-type-of-room --> |private room| mbu-type-of-birth

    mbu-type-of-birth --> |C-section delivery - 2-3 nights| mbu-stay(Stay)    
    mbu-type-of-birth --> |vaginal delivery - 1 night | mbu-stay

    mother-baby-unit --> |take appointment pediatrician| appointment-pediatrician{Appointment pediatrician}

    mbu-stay --> family-launch-zone(Family launch zone - B1 garage)
    car-seat-installed --> family-launch-zone
    appointment-pediatrician --> family-launch-zone
    family-launch-zone --> home-with-baby((Home with baby))
```