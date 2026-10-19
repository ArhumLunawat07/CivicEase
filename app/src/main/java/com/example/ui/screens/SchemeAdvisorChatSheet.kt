package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.ChatMessage
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeAdvisorChatSheet(
  messages: List<ChatMessage>,
  isGenerating: Boolean,
  onSendMessage: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  val starterPrompts = listOf(
    "How do I get an Income Certificate for scholarships?",
    "What does a DBT-enabled bank account mean?",
    "Can landless laborers qualify for Ayushman Bharat?",
    "How does PM MUDRA loan collateral exemption work?"
  )

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(messages.size - 1)
      }
    }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = WarmSurface,
    dragHandle = { BottomSheetDefaults.DragHandle() },
    modifier = Modifier
      .fillMaxHeight(0.9f)
      .testTag("scheme_advisor_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .imePadding()
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(AmberContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = AmberHighlight,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "AI Scheme Advisor",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = DeepNavyText
            )
            Text(
              text = "Powered by Gemini Intelligence",
              fontSize = 11.sp,
              color = SlateMuted
            )
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateMuted)
        }
      }

      // Disclaimer Banner
      Surface(
        color = WarmSurfaceVariant,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = ForestGreenPrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Civic advice is informational. Official portals decide final eligibility.",
            fontSize = 11.sp,
            color = SlateMuted
          )
        }
      }

      // Chat message list
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(messages, key = { it.id }) { msg ->
          ChatBubble(message = msg)
        }

        if (isGenerating) {
          item {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = ForestGreenPrimary,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Advisor is reviewing scheme guidelines...",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = SlateMuted
              )
            }
          }
        }
      }

      // Starter Prompts Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(starterPrompts) { prompt ->
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = WarmSurfaceVariant,
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
            modifier = Modifier.clickable { onSendMessage(prompt) }
          ) {
            Text(
              text = prompt,
              fontSize = 11.sp,
              color = DeepNavyText,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }

      // Input Row
      Surface(
        color = WarmSurface,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Ask about eligibility, documents, or rules...", fontSize = 13.sp) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("advisor_chat_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ForestGreenPrimary,
              unfocusedBorderColor = WarmBorder
            ),
            maxLines = 3
          )

          IconButton(
            onClick = {
              if (inputText.isNotBlank() && !isGenerating) {
                val toSend = inputText
                inputText = ""
                onSendMessage(toSend)
              }
            },
            enabled = inputText.isNotBlank() && !isGenerating,
            colors = IconButtonDefaults.iconButtonColors(
              containerColor = ForestGreenPrimary,
              contentColor = Color.White,
              disabledContainerColor = WarmBorder,
              disabledContentColor = SlateLight
            ),
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .testTag("advisor_chat_send_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun ChatBubble(message: ChatMessage) {
  val isUser = message.senderIsUser
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    Surface(
      shape = RoundedCornerShape(
        topStart = 14.dp,
        topEnd = 14.dp,
        bottomStart = if (isUser) 14.dp else 2.dp,
        bottomEnd = if (isUser) 2.dp else 14.dp
      ),
      color = if (isUser) ForestGreenPrimary else WarmSurfaceVariant,
      border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
      modifier = Modifier.widthIn(max = 300.dp)
    ) {
      Text(
        text = message.text,
        fontSize = 13.sp,
        color = if (isUser) Color.White else DeepNavyText,
        lineHeight = 18.sp,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
      )
    }
  }
}
