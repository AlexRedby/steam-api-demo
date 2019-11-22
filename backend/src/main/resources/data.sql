-- Test data for H2
-- Without time_created
INSERT INTO
    USERS  (id, community_visibility_state, nickname, primary_group_id, profile_state, small_avatar, last_update)
    VALUES ('76561197964586135', 1, 'TestNickname', '76561198015294053', 1, 'https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/52/523a331b47363ae4975b416bd66fbe7558cf8dc3.jpg', CURRENT_TIMESTAMP);