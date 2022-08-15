package net.dv8tion.jda.internal.utils;

import net.dv8tion.jda.api.entities.User;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public final class Mocks {
    public Mocks() {
        MockitoAnnotations.initMocks(this);

        BDDMockito.given(user.getId()).willReturn("-132774138746");
    }

    @Mock
    private User user;

    public User user() { return user; }
}
