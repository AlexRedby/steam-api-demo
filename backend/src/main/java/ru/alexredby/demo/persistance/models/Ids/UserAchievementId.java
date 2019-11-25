package ru.alexredby.demo.persistance.models.Ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.alexredby.demo.persistance.models.Achievement;
import ru.alexredby.demo.persistance.models.UserApplication;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAchievementId implements Serializable {
    private UserApplication userApplication;
    private Achievement achievement;
}
